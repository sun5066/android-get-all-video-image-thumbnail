/** 유틸리티 */
object UtilExample {

    /* 가장 최신의 이미지, 비디오 파일 썸네일 불러오기 */
    fun getRecentMediaUri(context: Context): Uri {
        val externalUri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE
        )
        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

        val cursorLoader = CursorLoader(context, externalUri, projection, selection,null,
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC")

        val cursor = cursorLoader.loadInBackground()!!
        val id = if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            cursor.getLong(index).toString()
        } else null
        cursor.close()

        return Uri.withAppendedPath(externalUri, id)
    }
}

/** 뷰모델 */
class ExampleViewModel: ViewModel() {
    // ...

    private fun setImageThumbnail() = runOnMainThread {
        CameraExample.getRecentMediaUri(context).apply { _photo.value = this }
    }
}

/** 데이터 바인딩 */
object ExampleBindingAdapter {

    @BindingAdapter("thumbnail")
    @JvmStatic
    fun setThumbnail(imageView: ImageView, uri: Uri?) {
        uri?.let {
            Glide.with(imageView)
                .load(it)
                .into(imageView)
        }
    }
}