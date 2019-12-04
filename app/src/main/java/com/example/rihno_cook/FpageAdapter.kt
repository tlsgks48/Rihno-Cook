package com.example.rihno_cook
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter


class FpageAdapter(fm : android.support.v4.app.FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) { // switch()문과 동일.
            0 -> {Menu1()}
            1 -> {Menu2()}
            2 -> {Menu3()}
            else -> {return Menu4()}
        }
    }

    override fun getCount(): Int {
        return 4 // 메뉴가 3개라서
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "메인"
            1 -> "레시피"
            2 -> "쿡TV"
            else -> {return "쿡톡"}
        }
    }

}