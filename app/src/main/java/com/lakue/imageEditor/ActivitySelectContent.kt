package com.lakue.imageEditor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.lakue.imageEditor.Adapter.RecyclerViewNormalAdapter
import com.lakue.imageEditor.Items.ItemContent
import com.lakue.imageEditor.type.RecyclerViewType
import kotlinx.android.synthetic.main.activity_select_content.*

class ActivitySelectContent : AppCompatActivity() {

    lateinit var adapter: RecyclerViewNormalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_content)

        initRecyclerView()

    }

    private fun initRecyclerView() {
        val numberOfColumns = 2 // 한줄에 3개의 컬럼을 추가합니다.

        val mGridLayoutManager = GridLayoutManager(this, numberOfColumns)
        mGridLayoutManager.isItemPrefetchEnabled = true

        rv_content.layoutManager = mGridLayoutManager
        adapter = RecyclerViewNormalAdapter(RecyclerViewType.CONTENT)
        adapter.setHasStableIds(true)
        rv_content.adapter = adapter
        addContentItem()

        adapter.setOnItemClickListener { view, item ->
//            var intent = Intent(this, ActivityContentDecorate::class.java)
//            intent.putExtra("EXTRA_CONTENT",item)
//            startActivityForResult(intent, Define.REQUEST_CODE_CONTENT)
            var resultIntent = Intent()
            resultIntent.putExtra("EXTRA_CONTENT",item)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Define.REQUEST_CODE_CONTENT){
                var resultIntent = Intent()
            }
        }

    }

    private fun addContentItem() {
        adapter.addItem(ItemContent(R.drawable.cgv))
        adapter.addItem(ItemContent(R.drawable.megabox))
        adapter.addItem(ItemContent(R.drawable.lotte))
        adapter.addItem(ItemContent(R.drawable.movie))
    }
}