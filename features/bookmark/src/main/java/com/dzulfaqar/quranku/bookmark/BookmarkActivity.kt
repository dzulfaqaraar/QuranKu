package com.dzulfaqar.quranku.bookmark

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dzulfaqar.quranku.bookmark.databinding.ActivityBookmarkBinding
import com.dzulfaqar.quranku.di.BookmarkModuleDependencies
import com.dzulfaqar.quranku.model.AyatModel
import com.dzulfaqar.quranku.ui.AyatAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import com.dzulfaqar.quranku.R as appR

class BookmarkActivity : AppCompatActivity(R.layout.activity_bookmark) {

    @Inject
    lateinit var factory: ViewModelFactory

    private val binding: ActivityBookmarkBinding by viewBinding(R.id.container)
    private val viewModel: BookmarkViewModel by viewModels { factory }

    private var ayatAdapter: AyatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerBookmarkComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    BookmarkModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        setupToolbar()

        ayatAdapter = AyatAdapter { ayat, isBookmark ->
            viewModel.bookmarkAyat(ayat, isBookmark)
        }

        setupView()
        setupObserver()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.also {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(appR.drawable.ic_arrow_back)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        with(binding.recyclerView) {
            itemTouchHelper.attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = ayatAdapter
        }
    }

    private fun setupObserver() {
        viewModel.listBookmark.observe(this) { data ->
            if (data.isEmpty()) {
                binding.layoutEmpty.root.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.layoutEmpty.root.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }

            ayatAdapter?.submitList(data)
        }
        viewModel.deletedData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { ayatModel ->
                showSnackbarDelete(ayatModel)
            }
        }
    }

    private fun showSnackbarDelete(ayatModel: AyatModel) {
        val snackbar = Snackbar.make(binding.root, R.string.confirm_deletion, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.ok) {
            viewModel.bookmarkAyat(ayatModel, true, askCanceling = false)
        }
        snackbar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.adapter = null
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swipedPosition = viewHolder.bindingAdapterPosition
            val ayatModel = ayatAdapter?.getSwipedData(swipedPosition)
            ayatModel?.let {
                viewModel.bookmarkAyat(it, false)
            }
        }
    })
}
