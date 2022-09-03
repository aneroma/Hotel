package ie.wit.hotel.views.login

import android.content.Intent
import android.os.Bundle
import android.util.Log.i
import android.view.View
import ie.wit.hotel.R
import ie.wit.hotel.views.BasePresenter
import ie.wit.hotel.views.BaseView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar.visibility = View.GONE

        info("Login activity started")

        presenter = initPresenter(BasePresenter(this)) as LoginPresenter

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        // [END config_signin]
//
//        app.googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        googleSignInButton.setOnClickListener{
//            presenter.googleSignIn()
//        }

        tvreg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                startActivity(Intent(this@LoginView, RegisterView::class.java ))
                info("Textview tvreg clicked\nRedirecting to register page")
            }
        })


        btnLogin.setOnClickListener {

            val passEmail = Lname.text.toString()
            val passPassword = Lpassword.text.toString()

            if (passEmail == "" || passPassword == "") {
                toast("Please provide email + password")
            }
            else {
                presenter.doLogin(passEmail, passPassword)
            }
        }

    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

}