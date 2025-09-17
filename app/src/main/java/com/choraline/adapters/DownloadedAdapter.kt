package com.choraline.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choraline.DownloadActivity
import com.choraline.R
import com.choraline.models.SongsData
import com.choraline.utils.AppController
import java.io.File

class DownloadedAdapter(
    private var list: MutableList<SongsData>,
    private val context: Context
) : RecyclerView.Adapter<DownloadedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.download_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(list[position], position)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtSongTitle: TextView = view.findViewById(R.id.row_songs_txtSongTitle)
        private val btnPlay: ImageButton = view.findViewById(R.id.row_songs_imgbtnPlay)
        private val btnDelete: ImageView = view.findViewById(R.id.delete_song)

        fun bindData(model: SongsData, pos: Int) {
            // Toggle delete visibility
            btnDelete.visibility = if (DownloadActivity.isEdit) View.VISIBLE else View.GONE

            txtSongTitle.text = model.title

            btnPlay.setImageResource(if (model.isPlaying) R.mipmap.play_on else R.mipmap.play)

            btnPlay.tag = model
            btnDelete.tag = model

            btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you wish to delete this download?")
                    .setPositiveButton("Confirm") { dialog: DialogInterface, _ ->
                        val data = it.tag as SongsData
                        val posToRemove = list.indexOf(data)
                        val file = File(data.songUrl)

                        if (file.delete()) {
                            AppController.dbInstance.deleteUrl(data.id)
                            list.removeAt(posToRemove)
                            notifyItemRemoved(posToRemove)
                            (context as? DownloadActivity)?.updateToolbarEdit()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                builder.show()
            }

            btnPlay.setOnClickListener(playClickListener)
        }

        private val playClickListener = View.OnClickListener { v ->
            val model = v.tag as SongsData
            val pos = list.indexOf(model)

            (context as? DownloadActivity)?.playsong(pos)

            // reset all
            list.forEach { it.isPlaying = false }

            // mark current as playing
            list[pos].isPlaying = true
            notifyDataSetChanged()
        }
    }

    fun refreshList(pos: Int) {
        list.forEach { it.isPlaying = false }
        if (pos in list.indices) {
            list[pos].isPlaying = true
        }
        notifyDataSetChanged()
    }
}
