package com.vango.orderprocessing.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import timber.log.Timber

object BindingUtils {

    @BindingAdapter("visible")
    @JvmStatic
    fun setVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            Glide
                .with(view)
                .load(it)
                .centerCrop()
                .into(view)
        }
    }

    @BindingAdapter("setAdapter")
    @JvmStatic
    fun setAdapter(
        recyclerView: RecyclerView,
        adapter: BaseAdapter<ViewDataBinding, ListAdapterItem>?
    ) {
        adapter?.let {
            recyclerView.adapter = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    @JvmStatic
    fun submitList(recyclerView: RecyclerView, list: List<ListAdapterItem>?) {
        val adapter = recyclerView.adapter as BaseAdapter<ViewDataBinding, ListAdapterItem>?
        Timber.e("submit list ${list?.size}")
        adapter?.updateData(list ?: listOf())
    }

    @BindingAdapter("loaderVisible")
    @JvmStatic
    fun <T: Any>setLoaderVisibility(view: View, list: List<T>?) {
        view.isVisible = list == null
    }

    @BindingAdapter("noDataVisible")
    @JvmStatic
    fun <T: Any>setNoDataVisibility(view: View, list: List<T>?) {
        view.isVisible = list != null && list.isEmpty()
    }

    @BindingAdapter("stringRes")
    @JvmStatic
    fun setStringRes(view: TextView, @StringRes resource: Int) {
        view.text = view.context.getString(resource)
    }

}