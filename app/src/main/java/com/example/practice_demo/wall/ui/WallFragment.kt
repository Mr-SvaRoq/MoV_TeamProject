package com.example.practice_demo.wall.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.profile.ui.ProfileFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WallFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Options menu
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wall, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        handleOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun handleOptionsItemSelected(item: MenuItem) {
        when(item.itemId) {
            R.id.menu_logout -> {
                // Vycisti saved preference
                activity?.let { SaveSharedPreference.clearUsername(it) }
                // Presmeruj na login
                findNavController().navigate(WallFragmentDirections.actionWallFragmentToLoginFragment())
            }

            R.id.menu_viewProfile -> {
                findNavController().navigate(WallFragmentDirections.actionWallFragmentToProfileFragment())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WallFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WallFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}