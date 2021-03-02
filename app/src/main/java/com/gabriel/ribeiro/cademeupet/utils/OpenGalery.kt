package com.gabriel.ribeiro.cademeupet.utils

import android.content.Intent

class OpenGalery {
    companion object{
        fun openGalery() : Intent{
            val intentOpen = Intent(Intent.ACTION_PICK)
            intentOpen.type = "image/*"
            return intentOpen
        }
    }

}

/*<androidx.appcompat.widget.LinearLayoutCompat
            android:id="@+id/linearLayoutCompat2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintTop_toBottomOf="@id/textViewNameAnimalDetail">



            <androidx.appcompat.widget.AppCompatTextView
                android:id="@+id/textViewSexDetail"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:layout_weight="0.5"
                android:gravity="center"
                android:textSize="16sp"
                app:fontFamily="@font/quicksand_medium"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/linearLayoutCompat2"
                tools:text="Sexo: Macho" />

        </androidx.appcompat.widget.LinearLayoutCompat>

        <androidx.appcompat.widget.LinearLayoutCompat
            android:id="@+id/linearLayoutCompat3"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:background="@color/white"
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
                android:drawableStart="@drawable/ic_comment"
                android:fontFamily="@font/quicksand_medium"
                android:padding="8dp" />

        </androidx.appcompat.widget.LinearLayoutCompat>

        <de.hdodenhof.circleimageview.CircleImageView
            android:id="@+id/imageViewUserPhotoDetail"
            android:layout_width="80dp"
            android:layout_height="80dp"
            android:layout_marginStart="16dp"
            android:layout_marginTop="8dp"
            app:civ_border_color="@color/colorOrange"
            app:civ_border_width="1dp"
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
            app:fontFamily="@font/quicksand_medium"
            app:layout_constraintBottom_toTopOf="@+id/buttonOpenChat"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/imageViewUserPhotoDetail"
            app:layout_constraintTop_toBottomOf="@+id/textViewPhoneDetail"
            app:layout_constraintVertical_bias="0"
            tools:text="teste@gmail.com" />

        <com.google.android.material.floatingactionbutton.FloatingActionButton
            android:id="@+id/buttonOpenChat"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:layout_marginBottom="16dp"
            android:background="?attr/selectableItemBackground"
            android:backgroundTint="@color/colorSalmao"
            android:src="@drawable/ic_send"
            android:visibility="gone"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/imageViewUserPhotoDetail" />

        <com.google.android.material.floatingactionbutton.FloatingActionButton
            android:id="@+id/buttonDeletePost"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="?attr/selectableItemBackground"
            android:backgroundTint="@color/colorSalmao"
            android:src="@drawable/ic_delete"
            android:visibility="visible"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/imageViewUserPhotoDetail" />


*/