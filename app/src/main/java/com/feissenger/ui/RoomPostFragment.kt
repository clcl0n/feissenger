package com.feissenger.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRoomPostBinding
import com.feissenger.ui.viewModels.RoomPostViewModel
import androidx.navigation.fragment.navArgs
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.DarkTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.giphy.sdk.ui.views.buttons.GPHGiphyButtonStyle
import kotlinx.android.synthetic.main.fragment_room_post.*


class RoomPostFragment : Fragment() {
    private lateinit var viewModel: RoomPostViewModel
    private lateinit var binding: FragmentRoomPostBinding
    private lateinit var sharedPref: MySharedPreferences

    val arg: RoomPostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = context?.let { MySharedPreferences(it) }!!

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room_post, container, false
        )
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(activity!!, Injection.provideViewModelFactory(context!!))
            .get(RoomPostViewModel::class.java)



        with(sharedPref) {
            viewModel.uid = get("uid").toString()
        }

        viewModel.roomId = arg.roomId
        viewModel.senderName = sharedPref.get("name").toString()

        binding.roomPost = viewModel


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment","roomsPost")
        editTextRoomPost.requestFocus()
        val keyboard =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(editTextRoomPost, 0)

        giphy_button_post.style = GPHGiphyButtonStyle.logo
        val settings =
            GPHSettings(gridType = GridType.carousel, dimBackground = true)
        when (sharedPref.get("theme")) {
            "light" -> settings.theme = LightTheme
            "dark" -> settings.theme = DarkTheme
        }
        val gifsDialog = GiphyDialogFragment.newInstance(settings)
        settings.mediaTypeConfig = arrayOf(GPHContentType.gif)

        giphy_button_post.setOnClickListener {
            fragmentManager?.let { it1 -> gifsDialog.show(it1, "gifs_dialog") }
            gifsDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                @SuppressLint("LogNotTimber")
                override fun onGifSelected(media: Media) {

                    viewModel.sendGif("gif:${media.id}")

                    val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                    sharedPref.put("fragment","roomMessages")
                    it.findNavController().popBackStack()
                }

                override fun onDismissed() {
                }
            }
        }

        button_post.setOnClickListener {
            viewModel.sendMessage()

            val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            sharedPref.put("fragment","roomMessages")
            it.findNavController().popBackStack()
        }
    }
    
}
