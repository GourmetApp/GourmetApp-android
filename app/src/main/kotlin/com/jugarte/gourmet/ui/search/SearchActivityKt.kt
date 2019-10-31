package com.jugarte.gourmet.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.javierugarte.searchtoolbar.SearchToolbarListener
import com.jugarte.gourmet.R
import com.jugarte.gourmet.domine.beans.Gourmet
import kotlinx.android.synthetic.main.search_activity_kt.*

class SearchActivityKt : AppCompatActivity(), SearchToolbarListener {

    companion object {
        private const val EXTRA_GOURMET = "extraGourmet"

        fun newStartIntent(context: Context, gourmet: Gourmet): Intent {
            val intent = Intent(context, SearchActivityKt::class.java)
            intent.putExtra(EXTRA_GOURMET, gourmet)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        searchToolbar.apply {
            setSupportActionBar(this)
            setSearchToolbarListener(this@SearchActivityKt)
            setHint(R.string.search_hint)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSearch(keyword: String?) {

    }

    override fun onClear() {

    }

}