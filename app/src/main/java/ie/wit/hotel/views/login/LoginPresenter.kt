package ie.wit.hotel.views.login


import com.google.firebase.auth.FirebaseAuth
import ie.wit.hotel.models.HotelFireStore
import ie.wit.hotel.views.BasePresenter
import ie.wit.hotel.views.BaseView
import ie.wit.hotel.views.VIEW
import org.jetbrains.anko.toast

class LoginPresenter (view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: HotelFireStore? = null

    init {
        if (app.hotels is HotelFireStore) {
            fireStore = app.hotels as HotelFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchHotels {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.LIST)
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }

//    fun googleSignIn() {
//        app.googleSignInClient.signInIntent
//        view?.navigateTo(VIEW.LIST)
//    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }
}