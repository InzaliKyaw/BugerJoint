package com.zg.burgerjoint.presenters.impls

import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zg.burgerjoint.data.model.BurgerModel
import com.zg.burgerjoint.data.model.impls.BurgerModelImpl
import com.zg.burgerjoint.data.model.impls.MockBurgerModelImpl
import com.zg.burgerjoint.data.vos.BurgerVO
import com.zg.burgerjoint.dummy.getDummyBurgers
import com.zg.burgerjoint.mvp.presenters.impls.MainPresenterImpl
import com.zg.burgerjoint.mvp.views.MainView
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class MainPresenterImplTest {
    private lateinit var mPresenter:MainPresenterImpl

    @RelaxedMockK
    private lateinit var mView:MainView

    private lateinit var mBurgerModel: BurgerModel

    @Before
    fun setUpPresenter(){
        MockKAnnotations.init(this)
        BurgerModelImpl.init(ApplicationProvider.getApplicationContext())
        mBurgerModel = MockBurgerModelImpl

        mPresenter = MainPresenterImpl()
        mPresenter.initPresenter(mView)
        mPresenter.mBurgerModel = this.mBurgerModel
    }

    @Test
    fun onTapAddToCart_callAddBurgerToCart(){
        val tapBurger = BurgerVO()
        tapBurger.burgerId = 1
        tapBurger.burgerName = "Big M"
        tapBurger.burgerImageUrl = ""
        tapBurger.burgerDescription = "Big M Desciption"

        var imageView = ImageView(ApplicationProvider.getApplicationContext())

        mPresenter.onTapAddToCart(tapBurger,imageView)

        verify {
            mView.addBurgerToCart(tapBurger,imageView)
        }
    }

    @Test
    fun onTapCart_callNavigateToCartScreen(){
        mPresenter.onTapCart()
        verify {
            mView.navigateToCartScreen()
        }
    }

    @Test
    fun onTapBurger_callNavigateToBurgerDetailsScreen(){
        val tapBurger = BurgerVO()
        tapBurger.burgerId = 1
        tapBurger.burgerName = "Big M"
        tapBurger.burgerImageUrl = ""
        tapBurger.burgerDescription = "Big M Desciption"

        var imageView = ImageView(ApplicationProvider.getApplicationContext())

        mPresenter.onTapBurger(tapBurger,imageView)

        verify {
            mView.navigateToBurgerDetailsScreen(tapBurger.burgerId,imageView)
        }
    }

    @Test
    fun onUIReady_callDisplayBurgerList_callDisplayCountInCart(){
        val lifecycleOwner = mock(LifecycleOwner::class.java)
        val lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycleRegistry)

        mPresenter.onUIReady(lifecycleOwner)

        verify {
            mView.displayBurgerList(getDummyBurgers())
        }
    }


}

