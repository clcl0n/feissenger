package com.feissenger.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.opengl.ETC1.getHeight
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
import com.feissenger.ui.adapter.MessagesAdapter
import com.feissenger.ui.viewModels.MessagesViewModel
import com.feissenger.R
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.ContactItemId
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
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_message.messages_list
import kotlinx.android.synthetic.main.fragment_room.*
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.message_item.view.*

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
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(MessagesViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
        }

        viewModel.contact = arg.contactId

        viewModel.contactName = arg.contactName

        binding.model = viewModel

//        viewModel.getContactById()

        viewModel.loadMessages()

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.messagesList.isNestedScrollingEnabled = false

        adapter = MessagesAdapter()
        binding.messagesList.adapter = adapter
        viewModel.messages.observe(this) {
            adapter.data = it
            binding.messagesList.scrollToPosition(adapter.itemCount - 1)
        }

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

        message_root.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.messagesList.scrollToPosition(adapter.itemCount - 1)
            }
        }

        message_input.addOnLayoutChangeListener { _, _, top, _, _, _, oldTop, _, _ ->
            if (top < oldTop) {
                binding.messagesList.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).myToolbar.toolbar_text.text = arg.contactName
    }
}