package com.feissenger.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.ui.adapter.MessagesAdapter
import com.feissenger.ui.viewModels.MessagesViewModel
import com.feissenger.R
import com.feissenger.databinding.FragmentMessageBinding
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.DarkTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.giphy.sdk.ui.views.buttons.GPHGiphyButtonStyle
import com.feissenger.data.util.Injection
import kotlinx.android.synthetic.main.fragment_message.*

class MessagesFragment : Fragment() {
    private lateinit var viewModel: MessagesViewModel
    private lateinit var binding: FragmentMessageBinding
    private lateinit var sharedPref: SharedPreferences

    val arg: MessagesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_message, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(MessagesViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = this.getString("uid", "").toString()
            viewModel.access = this.getString("access", "").toString()
        }

        viewModel.contact = arg.contactId

        binding.model = viewModel

        viewModel.loadMessages()

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = MessagesAdapter()
        binding.messagesList.adapter = adapter
        viewModel.messages.observe(this) {
            adapter.data = it
            binding.messagesList.scrollToPosition(adapter.itemCount-1)
        }

        val contentView = binding.messagesList
        contentView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            binding.messagesList.scrollToPosition(adapter.itemCount-1)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.edit().putString("fragment","messages").apply()
        sharedPref.edit().putString("contactId",arg.contactId).apply()

        giphy_button.style = GPHGiphyButtonStyle.iconSquareRounded
        val settings =
            GPHSettings(gridType = GridType.carousel, dimBackground = true)
        when (this.activity?.getPreferences(Activity.MODE_PRIVATE)?.getString("theme", "")) {
            "light" -> settings.theme = LightTheme
            "dark" -> settings.theme = DarkTheme
        }
        val gifsDialog = GiphyDialogFragment.newInstance(settings)
        settings.mediaTypeConfig = arrayOf(GPHContentType.gif)

        giphy_button.setOnClickListener {
            fragmentManager?.let { it1 -> gifsDialog.show(it1, "gifs_dialog") }
            gifsDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                @SuppressLint("LogNotTimber")
                override fun onGifSelected(media: Media) {
                    media.url?.let { it1 -> viewModel.sendGif(it1) }
                }

                override fun onDismissed() {
                }
            }
        }
    }
}