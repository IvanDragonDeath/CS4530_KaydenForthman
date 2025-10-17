package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = CourseDatabase.getDatabase(applicationContext)
        val repository = CourseRepository.getInstance(database.courseDao())
        val viewModelFactory = MyViewModelFactory(repository)
        val viewModel: MyViewModel = ViewModelProvider(this, viewModelFactory)[MyViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            CourseApp(viewModel)
        }
    }
}
// The function that decides what information is displayed when you click a button
@Composable
fun CourseApp(viewModel: MyViewModel) {
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var showAddCourse by remember {mutableStateOf(false)}
    var editedCourse by remember { mutableStateOf<Course?>(null) }
    val courseList by viewModel.courses.collectAsState()

    // Add course information
    if(showAddCourse){
        AddCourseScreen(
            onAdd = { department, number, location ->
                viewModel.addCourse(
                    department, number, location)
                showAddCourse = false
            },
            onCancel = { showAddCourse = false}
        )
    }
    // if the details info is selected
    else if(selectedCourse != null){
        DetailsScreen(
            course = selectedCourse!!,
            onDelete = {
                viewModel.deleteCourse(it)
                selectedCourse = null
            },
            onBack = {selectedCourse = null},
            onEdit = { course ->
                editedCourse = course
                selectedCourse = null
            }
        )
    }
    // if the edit info button is selected
    else if (editedCourse != null){
        EditCourseScreen(
            course = editedCourse!!,
            onSave = { course ->
                viewModel.editCourse(course)
                editedCourse = null
            },
            onCancel = { editedCourse = null}
        )
    }
    // else display the list info
    else {
        ListScreen(
            courses = courseList,
            onCourseClick = {selectedCourse = it },
            onAddClick = { showAddCourse = true }
        )
    }
}

// list out all the current courses that have been created
@Composable
fun ListScreen(
    courses: List<Course>,
    onCourseClick: (Course) -> Unit,
    onAddClick: () -> Unit
) {
    Column {
        Button(onClick = onAddClick, modifier = Modifier.padding(16.dp)) {
            Text("Add Course")
        }
        LazyColumn {
            items(courses) { course ->
                Text(
                    text = course.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCourseClick(course) }
                        .padding(16.dp)
                )
            }
        }
    }
}
// edit the details of the currently selected course
@Composable
fun EditCourseScreen (
    course: Course,
    onSave: (Course) -> Unit,
    onCancel: () -> Unit
) {
    var department by remember { mutableStateOf(course.department) }
    var number by remember { mutableStateOf(course.number) }
    var location by remember { mutableStateOf(course.location) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") }
        )

        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Course Number") }
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                val updatedCourse = course.copy(
                    department = department,
                    number = number,
                    location = location
                )
                onSave(updatedCourse)
            }) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}
// Add a new course into the data
@Composable
fun AddCourseScreen(
    onAdd: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var department by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") }
        )

        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Course Number") }
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Spacer(Modifier.height(15.dp))
            Button(onClick = {
                if (department.isNotBlank() && number.isNotBlank() && location.isNotBlank()) {
                    onAdd(department, number, location)
                }
            }) {
                Text("Add")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}
// display the details about a selected course
@Composable
fun DetailsScreen(
    course: Course,
    onDelete: (Course) -> Unit,
    onBack: () -> Unit,
    onEdit: (Course) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Department: ${course.department}")
        Text("Course Number: ${course.number}")
        Text("Location: ${course.location}")

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { onEdit(course) }) {
                Text("Edit")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { onDelete(course) }) {
                Text("Delete")
            }
        }
    }
}