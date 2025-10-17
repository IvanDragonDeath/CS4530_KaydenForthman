package com.example.assignment2

import kotlinx.coroutines.flow.Flow

class CourseRepository private constructor(private val courseDao: CourseDao) {

    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun addCourse(course: Course) = courseDao.insert(course)
    suspend fun editCourse(course: Course) = courseDao.update(course)
    suspend fun deleteCourse(course: Course) = courseDao.delete(course)

    companion object {
        @Volatile
        private var INSTANCE: CourseRepository? = null

        fun getInstance(courseDao: CourseDao): CourseRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = CourseRepository(courseDao)
                INSTANCE = instance
                instance
            }
        }
    }
}