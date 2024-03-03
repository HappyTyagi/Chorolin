package com.choraline.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.DownloadActivity
import com.choraline.PurchasedMusicSongListActivity
import com.choraline.R
import com.choraline.models.SongsData
import com.choraline.utils.AppController
import kotlinx.android.synthetic.main.download_row.view.*

import java.io.File
import android.content.DialogInterface
import android.R.string.cancel
import android.app.AlertDialog


class DownloadedAdapter(private var list:ArrayList<SongsData>,private var context:Context) : RecyclerView.Adapter<DownloadedAdapter.MyViewHolder>() {



    private val mHandler = Handler();
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.download_row,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
      return  list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder!!.bindData(list.get(position),position)

    }


    inner class MyViewHolder(view:View) : RecyclerView.ViewHolder(view) {

        fun bindData(model:SongsData,pos: Int)
        {

             if (DownloadActivity.isEdit)
                    itemView.delete_song.visibility = View.VISIBLE
                else itemView.delete_song.visibility = View.GONE
                itemView.visibility=View.VISIBLE
                itemView.row_songs_txtSongTitle.text = model.title

                if(model.isPlaying)
                    itemView.row_songs_imgbtnPlay.setImageResource(R.mipmap.play_on)
                else
                    itemView.row_songs_imgbtnPlay.setImageResource(R.mipmap.play)
                itemView.row_songs_imgbtnPlay.setTag(model)
                itemView.delete_song.tag = model
                itemView.delete_song.setOnClickListener{


                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Are you sure you wish to delete this download?")
                            .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                                // FIRE ZE MISSILES!
                                val data = it.tag as SongsData
                                val  pos = list.indexOf(data)
                                val file = File(data.songUrl)

                                if(file.delete())
                                {
                                    AppController.dbInstance.deleteUrl(data.id)
                                    //    (context.applicationContext as AppController).removeUrl(data)
                                    list.remove(data)
                                    notifyItemRemoved(pos)
                                    (context as DownloadActivity).updateToolbarEdit()

                                }
                                dialog.dismiss()

                            })
                            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                                // User cancelled the dialog
                                dialog.dismiss()
                            })

                    builder.create()
                    builder.show()


            }
            itemView.row_songs_imgbtnPlay.setOnClickListener(playClickListener)
        }
        var playClickListener: View.OnClickListener = View.OnClickListener { v ->
            val model = v.tag as SongsData
            val pos = list.indexOf(model)
            (context as DownloadActivity).playsong(pos)
            for(vd in list)
            {
                vd.isPlaying=false
            }
            (list [pos]).isPlaying=true
            notifyDataSetChanged()
        }
    }
    fun refreshList(pos: Int) {
        for(i in 0..list!!.size-1)
        {
            this.list!!.get(i).isPlaying=false
        }
        if(pos>=0 && pos<this.list!!.size)
            this.list!!.get(pos).isPlaying=true
        notifyDataSetChanged()
    }





}