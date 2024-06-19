package com.example.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tasks")
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "imgByteArray")
    var imageByteArray: ByteArray? = null,

    @ColumnInfo(name = "dateTime")
    var dateTime: String? = null,

    @ColumnInfo(name = "subtitle")
    var subtitle: String? = null,

    @ColumnInfo(name = "noteText")
    var noteText: String? = null,

    @ColumnInfo(name = "imagePath")
    var imagePath: String? = null,

    @ColumnInfo(name = "colorArgb")
    var colorArgb: Int = 0xFFFFFFFF.toInt(),

    @ColumnInfo(name = "webLink")
    var webLink: String? = null,

    @ColumnInfo(name = "priority")
    var priority: Int = 0,

    @ColumnInfo(name = "isCompleted")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "category")
    var category: String? = null
) : Serializable {

    override fun toString(): String {
        return "$title：$dateTime"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskModel

        if (id != other.id) return false
        if (title != other.title) return false
        if (imageByteArray != null) {
            if (other.imageByteArray == null) return false
            if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        } else if (other.imageByteArray != null) return false
        if (dateTime != other.dateTime) return false
        if (subtitle != other.subtitle) return false
        if (noteText != other.noteText) return false
        if (imagePath != other.imagePath) return false
        if (colorArgb != other.colorArgb) return false
        if (webLink != other.webLink) return false
        if (priority != other.priority) return false
        if (isCompleted != other.isCompleted) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (imageByteArray?.contentHashCode() ?: 0)
        result = 31 * result + (dateTime?.hashCode() ?: 0)
        result = 31 * result + (subtitle?.hashCode() ?: 0)
        result = 31 * result + (noteText?.hashCode() ?: 0)
        result = 31 * result + (imagePath?.hashCode() ?: 0)
        result = 31 * result + colorArgb
        result = 31 * result + (webLink?.hashCode() ?: 0)
        result = 31 * result + priority
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + (category?.hashCode() ?: 0)
        return result
    }
}
