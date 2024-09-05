package com.example.movielistproj

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movielistproj.databinding.FragmentFirstBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class FirstFragment : Fragment() {
    val api =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZDcxYzQyYzAzMzhjNTVmOTgyZDg1ZjFhMzU3YzhlNCIsInN1YiI6IjY0YjgwMDkzZDM5OWU2MDBjYTkwOTcxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vsba7LUmlEXkl0OlEUKhYyrVY1Q7FZ_B0GAFLHgvkiY"

    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val response = client.request("https://api.themoviedb.org/3/movie/now_playing") {
                method = HttpMethod.Get
                bearerAuth(api)
            }.body<MovieResponse>()
            val adapter = ListAdapter(requireContext())
            binding.movieList.adapter = adapter
            adapter.setData(response.results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ListAdapter(private val context: Context) : BaseAdapter() {
    val movieList = mutableListOf<Movie>()

    fun setData(Movies: List<Movie>) {
        movieList.clear()
        movieList.addAll(Movies)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return movieList.size
    }

    override fun getItem(position: Int): Movie {
        return movieList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var newView = convertView
        val inflater = LayoutInflater.from(context)

        val movie = getItem(position)
        if (newView == null) {
            newView = inflater.inflate(R.layout.movie_item, parent, false)
        }
        val movieName: TextView = newView!!.findViewById(R.id.movieName)
        val movieDate: TextView = newView!!.findViewById(R.id.movieDate)

        movieName.text = movie.title
        movieDate.text = movie.releaseDate

        return newView
    }

}