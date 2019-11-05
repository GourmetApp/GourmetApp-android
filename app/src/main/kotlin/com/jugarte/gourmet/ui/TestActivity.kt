package com.jugarte.gourmet.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jugarte.gourmet.R
import com.jugarte.gourmet.domine.LoginResponse
import com.jugarte.gourmet.domine.LoginUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newStartIntent(context: Context): Intent {
            return Intent(context, TestActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        LoginUseCase().execute("", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { success: LoginResponse ->
                    textView.text = success.current
                }
                .dispose()
    }

}
