package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.data.GetUser
import com.gabriel.ribeiro.cademeupet.databinding.FragmentDetailBinding
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.ui.adatper.SlidePagerAdapter
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_KEY
import com.gabriel.ribeiro.cademeupet.utils.DeletePostDialog
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding : FragmentDetailBinding get()= _binding!!

    private lateinit var post : Post
    private lateinit var uidTo : String
    private val deletePostDialog by lazy {
        activity?.let {
            DeletePostDialog(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().let {
            post = it.getParcelable(POST_KEY)!!
            setValuesToWidgets(post)
        }
        binding.buttonOpenChat.setOnClickListener {
            val uidToBundle = Bundle().apply {
                putString("uidTo",uidTo)
            }
            findNavController().navigate(R.id.action_detailFragment_to_chatFragment,uidToBundle)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setValuesToWidgets(post: Post) {
        binding.progressBarDetails.visibility = View.VISIBLE
        post.apply {
            //Picasso.with(activity).load(animal?.imageAnimal).into(binding.imageViewAnimalDetail)
           // binding.collapsingToolbarDetails.title = animal?.name
            binding.textViewSexDetail.text = "Sexo: ${animal?.sex}"
            binding.textViewSizeDetail.text = "Tamanho: ${animal?.size}"
            binding.textViewStatusAndDateDetail.text = "${animal?.status} em ${post.date}"
            binding.textViewAddressDetail.text = "${address?.street} - ${address?.city} " +
                    "- ${address?.neighborhood} - ${address?.cep}"
            binding.textViewCommentDetail.text = comment
            val imagesList : MutableList<String> = ArrayList()
            imagesList.addAll(post.animal?.images!!)
            Log.d(Constants.TAG, "setValuesToWidgets: Listade images ; $imagesList")
            val adapterPager = SlidePagerAdapter(imagesList)

            binding.viewPagerDetatail.adapter = adapterPager
            //binding.tabLayoutDetail.setupWithViewPager(binding.viewPagerDetatail,true)

            if (idUser != FirebaseInstances.getFirebaseAuth().currentUser?.uid) {
                binding.buttonOpenChat.visibility = View.VISIBLE
                binding.buttonDeletePost.visibility = View.GONE
            }
            binding.progressBarDetails.visibility = View.GONE
        }

        GetUser.getUser(post.idUser!!, object : GetUser.UserFirestoreCallback {
            override fun onGetUser(user: User) {
                user.apply {
                    if (uid != null) {
                        uidTo = uid
                    }
                    Picasso.with(activity).load(imageProfile).into(binding.imageViewUserPhotoDetail)
                    binding.textViewNameUserDetail.text = name
                    binding.textViewPhoneDetail.text = phone
                    binding.textViewEmailUserDetail.text = email
                }
            }
        })
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
    /*<com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/collapsingToolbarDetails"
            android:layout_width="match_parent"
            android:layout_height="match_parent"

            android:fitsSystemWindows="true"
            app:contentScrim="?attr/colorPrimary"
            app:layout_scrollFlags="scroll|snap|exitUntilCollapsed">

            <androidx.appcompat.widget.AppCompatImageView
                android:id="@+id/imageViewAnimalDetail"
                android:layout_width="match_parent"
                android:layout_height="320dp"
                app:layout_collapseMode="parallax"
                android:scaleType="fitXY" />

            <androidx.appcompat.widget.AppCompatImageView
                android:id="@+id/appCompatImageView"
                android:layout_width="match_parent"
                android:layout_height="180dp"
                app:layout_collapseMode="parallax"
                android:background="@drawable/bg_gradient_dark"
                android:layout_gravity="bottom" />

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbarDetails"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
                app:layout_collapseMode="pin">

            </androidx.appcompat.widget.Toolbar>

        </com.google.android.material.appbar.CollapsingToolbarLayout>



    </com.google.android.material.appbar.AppBarLayout>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/colorBackground"
        app:layout_behavior="com.google.android.material.appbar.AppBarLayout$ScrollingViewBehavior">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <ProgressBar
                android:id="@+id/progressBarDetails"
                style="?android:attr/progressBarStyle"
                android:layout_width="wrap_content"
                android:visibility="gone"
                android:clickable="true"
                android:layout_height="wrap_content"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <androidx.appcompat.widget.LinearLayoutCompat
                android:id="@+id/linearLayoutCompat2"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:padding="8dp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent">

                <androidx.appcompat.widget.AppCompatTextView
                    android:id="@+id/textViewSizeDetail"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_weight="0.5"
                    android:drawableStart="@drawable/ic_size"
                    android:gravity="center"
                    android:textSize="16sp"
                    app:fontFamily="@font/quicksand_medium"
                    tools:text="Tamanho: Pequeno" />

                <androidx.appcompat.widget.AppCompatTextView
                    android:id="@+id/textViewSexDetail"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:gravity="center"
                    android:textSize="16sp"
                    android:layout_weight="0.5"
                    app:fontFamily="@font/quicksand_medium"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintTop_toBottomOf="@+id/linearLayoutCompat2"
                    tools:text="Sexo: Macho" />


            </androidx.appcompat.widget.LinearLayoutCompat>

            <androidx.appcompat.widget.LinearLayoutCompat
                android:id="@+id/linearLayoutCompat3"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/linearLayoutCompat2">

                <androidx.appcompat.widget.AppCompatTextView
                    android:id="@+id/textViewStatusAndDateDetail"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:textSize="16sp"
                    app:fontFamily="@font/quicksand_medium"
                    tools:text="Perdido em 20/20/20" />

                <androidx.appcompat.widget.AppCompatTextView
                    android:id="@+id/textViewAddressDetail"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:drawableStart="@drawable/ic_location"
                    android:gravity="center"
                    android:padding="8dp"
                    android:textSize="16sp"
                    android:textStyle="bold"
                    app:fontFamily="@font/quicksand_medium"
                    tools:text="Rua tal tal tal n 2020" />

                <androidx.appcompat.widget.AppCompatTextView
                    android:id="@+id/textViewCommentDetail"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:padding="8dp"
                    android:drawableStart="@drawable/ic_comment"
                    android:fontFamily="@font/quicksand_medium"/>

            </androidx.appcompat.widget.LinearLayoutCompat>

            <de.hdodenhof.circleimageview.CircleImageView
                android:id="@+id/imageViewUserPhotoDetail"
                android:layout_width="80dp"
                android:layout_height="80dp"
                app:civ_border_color="@color/colorOrange"
                app:civ_border_width="1dp"
                android:layout_marginStart="16dp"
                android:layout_marginTop="8dp"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/linearLayoutCompat3" />

            <androidx.appcompat.widget.AppCompatTextView
                android:id="@+id/textViewNameUserDetail"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginStart="5dp"
                android:layout_marginTop="5dp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toEndOf="@+id/imageViewUserPhotoDetail"
                app:layout_constraintTop_toTopOf="@+id/imageViewUserPhotoDetail"
                tools:text="Nome" />

            <androidx.appcompat.widget.AppCompatTextView
                android:id="@+id/textViewPhoneDetail"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="5dp"
                app:fontFamily="@font/quicksand_medium"
                app:layout_constraintStart_toEndOf="@+id/imageViewUserPhotoDetail"
                app:layout_constraintTop_toBottomOf="@+id/textViewNameUserDetail"
                tools:text="92870244" />

            <androidx.appcompat.widget.AppCompatTextView
                android:id="@+id/textViewEmailUserDetail"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginStart="5dp"
                app:layout_constraintVertical_bias="0"
                app:fontFamily="@font/quicksand_medium"
                app:layout_constraintBottom_toTopOf="@+id/buttonOpenChat"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toEndOf="@+id/imageViewUserPhotoDetail"
                app:layout_constraintTop_toBottomOf="@+id/textViewPhoneDetail"
                tools:text="teste@gmail.com" />

            <com.google.android.material.floatingactionbutton.FloatingActionButton
                android:id="@+id/buttonOpenChat"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="12dp"
                android:visibility="gone"
                android:background="?attr/selectableItemBackground"
                android:backgroundTint="@color/colorSalmao"
                android:src="@drawable/ic_send"
                android:layout_marginBottom="16dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/imageViewUserPhotoDetail" />

            <com.google.android.material.floatingactionbutton.FloatingActionButton
                android:id="@+id/buttonDeletePost"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="12dp"
                android:visibility="visible"
                android:background="?attr/selectableItemBackground"
                android:backgroundTint="@color/colorSalmao"
                android:src="@drawable/ic_delete"
                android:layout_marginBottom="16dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/imageViewUserPhotoDetail"/>

        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.core.widget.NestedScrollView>*/
}