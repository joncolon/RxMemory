import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.View
import android.widget.Toast

fun executeInThread(function: () -> Unit) {
    Thread({ function() }).start()
}

fun runOnMainThread(runnable: () -> Unit) {
    Handler(Looper.getMainLooper()).post(runnable)
}

fun Any.DEBUG(message: String) {
    Log.d(javaClass.simpleName, message)
}

fun Any.ERROR(message: String) {
    Log.e(javaClass.simpleName, message)
}

fun Context.isNetworkStatusAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let {
        it.activeNetworkInfo?.let {
            if (it.isConnected) return true
        }
    }
    return false
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.getResourceEntryName(): String = this.context.resources.getResourceEntryName(this.id)

fun FragmentActivity.showFragment(fragmentToShow: Fragment, frameId: Int, tag: String?, addToBackStack: Boolean) {
    var fragmentDisplayed: Fragment? = supportFragmentManager.findFragmentByTag(fragmentToShow.tag)
    if (fragmentDisplayed == null) {
        fragmentDisplayed = fragmentToShow
        replaceFragment(fragmentDisplayed, frameId, tag, addToBackStack)
    } else {
        show(fragmentToShow)
    }
}

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String?, addToBackStack: Boolean) {
    when (addToBackStack) {
        true -> supportFragmentManager.inTransaction {
            addToBackStack(tag)
            replace(frameId, fragment, tag)
        }
        false -> supportFragmentManager.inTransaction { replace(frameId, fragment, tag) }
    }
}

fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int, tag: String?, addToBackStack: Boolean) {
    when (addToBackStack) {
        true -> supportFragmentManager.inTransaction {
            addToBackStack(tag)
            add(frameId, fragment, tag)
        }
        false -> supportFragmentManager.inTransaction { add(frameId, fragment, tag) }
    }
}

fun FragmentActivity.show(fragment: Fragment) {
    supportFragmentManager.inTransaction {
        show(fragment)
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun FragmentActivity.removeFragment(fragment: Fragment, tag: String?) {
    val fragmentDisplayed: Fragment? = supportFragmentManager.findFragmentByTag(tag)
    if (fragmentDisplayed != null) {
        removeFragment(fragment, tag)
    }
}