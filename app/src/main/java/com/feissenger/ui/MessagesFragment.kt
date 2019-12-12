package com.feissenger.ui


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
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
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_message.*

class MessagesFragment : Fragment() {
    private lateinit var viewModel: MessagesViewModel
    private lateinit var binding: FragmentMessageBinding
    private lateinit var sharedPref: MySharedPreferences
    private lateinit var adapter: MessagesAdapter

    val arg: MessagesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = context?.let { MySharedPreferences(it) }!!
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_message, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(activity!!, Injection.provideViewModelFactory(context!!))
            .get(MessagesViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
        }

        viewModel.contact = arg.contactId

        viewModel.contactName = arg.contactName

        viewModel.senderName = sharedPref.get("name").toString()

        binding.model = viewModel

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.messagesList.isNestedScrollingEnabled = false

        adapter = MessagesAdapter(Glide.with(this), this.context!!)
        binding.messagesList.adapter = adapter

        viewModel.messages.observeForever {
            adapter.data = it
            binding.messagesList.scrollToPosition(adapter.itemCount - 1)
        }

        viewModel.loadMessages()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment", "messages")
        sharedPref.put("contactId", arg.contactId)
        sharedPref.put("contactName", arg.contactName)

        giphy_button.style = GPHGiphyButtonStyle.iconSquareRounded
        val settings =
            GPHSettings(gridType = GridType.carousel, dimBackground = true)
        when (sharedPref.get("theme")) {
            "light" -> settings.theme = LightTheme
            "dark" -> settings.theme = DarkTheme
        }
        val gifsDialog = GiphyDialogFragment.newInstance(settings)
        settings.mediaTypeConfig = arrayOf(GPHContentType.gif)

        giphy_button_layout.setOnClickListener {
            fragmentManager?.let { it1 -> gifsDialog.show(it1, "gifs_dialog") }
            gifsDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                @SuppressLint("LogNotTimber")
                override fun onGifSelected(media: Media) {
                    viewModel.sendGif("gif:${media.id}")
                }

                override fun onDismissed() {
                }
            }
        }

        message_root.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.messagesList.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).myToolbar.toolbar_text.text = arg.contactName
        (activity as MainActivity).myToolbar.theme_icon.visibility = View.GONE
    }
}