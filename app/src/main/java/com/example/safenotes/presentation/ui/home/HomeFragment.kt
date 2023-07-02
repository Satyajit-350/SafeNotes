package com.example.safenotes.presentation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.safenotes.R
import com.example.safenotes.databinding.FragmentHomeBinding
import com.example.safenotes.models.notes.NotesResponse
import com.example.safenotes.adapters.CategoryAdapter
import com.example.safenotes.adapters.NoteAdapter2
import com.example.safenotes.presentation.ui.home.fragments.AudioFragment
import com.example.safenotes.presentation.ui.home.fragments.DocumentsFragment
import com.example.safenotes.presentation.ui.home.fragments.ImageFragment
import com.example.safenotes.presentation.ui.home.fragments.LinksFragment
import com.example.safenotes.presentation.bottomSheet.BottomSheetNotes
import com.example.safenotes.utils.NetworkResult
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var selectionPageAdapter: SelectionPageAdapter? = null

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private var categoryAdapter: CategoryAdapter?=null

    private var noteAdapter: NoteAdapter2?=null

    private var dataReceived= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        if (savedInstanceState?.getBoolean("dataPresent")==true){
            dataReceived= true
        }
        noteAdapter = NoteAdapter2(::onNoteClicked)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteAdapter?.notifyDataSetChanged()

        binding.categoryRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.notesRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        //category
        homeViewModel.category.observe(viewLifecycleOwner, Observer {
            categoryAdapter = CategoryAdapter(it, ::onCategoryClicked)
            binding.categoryRv.adapter = categoryAdapter
        })

        binding.notesRv.adapter = noteAdapter

        //notes
        homeViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    dataReceived= true
                    noteAdapter?.submitList(it.data)
                }

                is NetworkResult.Message -> {}
            }
        })


        //quotes
        homeViewModel.quotesLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Message -> {}
                is NetworkResult.Success -> {
                    dataReceived= true
                    binding.quotesTv.text = it.data!!.quote
                    binding.quotesAuthTv.text =  "- ${it.data.author}"
                    Picasso.get().load(it.data.image).placeholder(R.drawable.sherlock).into(binding.circularPersonImg)
                }
            }
        })

        if (!dataReceived){
            homeViewModel.getNotes()
            homeViewModel.getQuote()
        }

        handleViewPager()

        binding.editIv.setOnClickListener {
            openNotesDialog()
        }

    }

    private fun openNotesDialog() {
        val bottomSheetNotes  = BottomSheetNotes()
        bottomSheetNotes.show(childFragmentManager,null)
    }

    private fun handleViewPager() {
        selectionPageAdapter =
            SelectionPageAdapter(childFragmentManager, requireActivity().lifecycle)
        selectionPageAdapter!!.addFragment(ImageFragment(), "Images")
        selectionPageAdapter!!.addFragment(AudioFragment(), "Audios")
        selectionPageAdapter!!.addFragment(DocumentsFragment(), "Docs")
        selectionPageAdapter!!.addFragment(LinksFragment(), "Links")
        binding.viewpager.adapter = selectionPageAdapter
        //        binding.viewpager.isUserInputEnabled = false
        binding.viewpager.offscreenPageLimit = 3

        TabLayoutMediator(
            binding.tabLayout, binding.viewpager, true
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = selectionPageAdapter!!.fragmentTitleList[position]
        }.attach()
        for (i in 0 until binding.tabLayout.tabCount) {
            binding.tabLayout.getTabAt(i)
        }
    }

    private fun onNoteClicked(noteResponse: NotesResponse){
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse)) //gson helps us to convert object into string
        findNavController().navigate(R.id.action_nav_home_to_editNote, bundle)
    }

    internal class SelectionPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        val fragmentTitleList: MutableList<String> = ArrayList()
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getItemCount(): Int {
            return fragmentList.size
        }
    }

    private fun onCategoryClicked(category: String){
//        findNavController().navigate(R.id.action_mainFragment_to_notesFragment, bundle)
        Toast.makeText(context,category,Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (dataReceived){
            outState.putBoolean("dataPresent", true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}