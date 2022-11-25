package com.example.appposvendas.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagaAdapter(fragment: HomeActivity) :  FragmentStateAdapter(fragment) {
    private val fragmentslist = mutableListOf<Fragment>()
    private val titlelist = mutableListOf<String>()

    fun getTitle(position: Int): String{
        return titlelist.get(position)
    }
    fun addFragmentes(fragment: Fragment,title: String){
    fragmentslist.add(fragment)
    titlelist.add(title)
}
    override fun getItemCount(): Int {
        return fragmentslist.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentslist.get(position)
    }
}