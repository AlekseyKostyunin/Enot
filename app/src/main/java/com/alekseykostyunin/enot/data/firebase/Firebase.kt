package com.alekseykostyunin.enot.data.firebase

import android.util.Log
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.entities.Photo
import com.alekseykostyunin.enot.domain.entities.StatusOrder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

object Firebase {

    private var auth = Firebase.auth
    private val user = auth.currentUser
    private val database = Firebase.database.reference

    /* User*/
    fun currentUser(): Boolean {
        Log.d("TEST_currentUser_MyFirebaseAuth1", user.toString())
        return if (user == null) {
            Log.d("TEST_currentUser_null", "not")
            false
        } else {
            Log.d("TEST_currentUser_MyFirebaseAuth2", user.uid)
            true
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TEST_sign", "signInWithEmail:success")
                    onResult(true)
                } else {
                    Log.w("TEST_sign", "signInWithEmail:failure", task.exception)
                }
            }
            .addOnFailureListener {
                Log.w("TEST_", it.toString())
                onResult(false)
            }
    }

    fun singOutUser() {
        auth.signOut()
        Log.d("TEST_singOutUser", "ok")
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d("TEST_resetPassword", "yes$it")
                onResult(true)
            }.addOnFailureListener {
                Log.d("TEST_resetPassword", "not" + it.message)
                onResult(false)
            }
    }

    fun reg(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TEST_reg", "createUserWithEmail:success")
                    val user =
                        auth.currentUser ?: return@addOnCompleteListener
                    val userId = user.uid
                    database.child("users").child(userId).child("email")
                        .setValue(email)
                    onResult(true)
                } else {
                    Log.w(
                        "TEST_reg",
                        "createUserWithEmail:failure",
                        task.exception
                    )
                    onResult(false)
                }
            }
    }

    /* Orders */
    fun getAllOrders(): Flow<List<Order>> = callbackFlow {
        if (user != null) {
            val userId = user.uid
            val db = database.child("users").child(userId).child("orders")
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orders = mutableListOf<Order>()
                    for (i in snapshot.children) {
                        val order = i.getValue(Order::class.java)
                        if (order != null) {
                            orders.add(order)
                        }
                    }
                    launch { send(orders.asReversed()) }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            }
            db.addValueEventListener(valueEventListener)
            awaitClose { db.removeEventListener(valueEventListener) }
        }
    }

    fun addOrder(order: Order) {
        if (user != null) {
            val userId = user.uid
            val idOrder = database.child("users")
                .child(userId)
                .child("orders")
                .push().key.toString()
            database.child("users").child(userId)
                .child("orders")
                .child(idOrder).setValue(order.copy(id = idOrder))
        }
    }

    fun closeOrder(order: Order): Flow<Order> = callbackFlow {
        if (user != null) {
            val userId = user.uid
            val idOrder = order.id
            val dateCloseOrder = DateUtil.dateOfUnit
            val history = order.history?.toMutableList()
            history?.let {
                val oldHistoryStep = history.last().apply {
                    type = 1
                }
                val odlIdHistoryStep = oldHistoryStep.id

                val newIdHistoryStep = odlIdHistoryStep.plus(1)
                val newHistoryStep = HistoryStep(
                    newIdHistoryStep,
                    dateCloseOrder,
                    3,
                    "Заказ выполнен"
                )
                history.add(newHistoryStep)
                Log.d("TEST_history", history.toString())
            }

            val orderUpdate = Order(
                id = idOrder,
                status = StatusOrder.CLOSED,
                client = order.client,
                dateAdd = order.dateAdd,
                dateClose = dateCloseOrder,
                description = order.description,
                type = order.type,
                model = order.model,
                priceZip = order.priceZip,
                priceWork = order.priceWork,
                history = history,
                photos = order.photos,
                comment = order.comment,
            )

            if (idOrder != null) {
                database
                    .child("users")
                    .child(userId)
                    .child("orders")
                    .child(idOrder)
                    .setValue(orderUpdate)

                val db = database
                    .child("users")
                    .child(userId)
                    .child("orders")
                    .child(idOrder)

                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val closedOrder = snapshot.getValue(Order::class.java)
                        if (closedOrder != null) {
                            launch { send(closedOrder) }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("TEST_snapshot_error", error.message)
                    }
                }
                db.addValueEventListener(valueEventListener)
                awaitClose { db.removeEventListener(valueEventListener) }

            }
        }
    }

    fun editOrder(order: Order) {
        if (user != null) {
            val userId = user.uid
            order.id?.let { idOrder ->
                database
                    .child("users")
                    .child(userId)
                    .child("orders")
                    .child(idOrder)
                    .setValue(order)
            }
        }

    }

    fun addPhotoOrder(photoUri: String, order: Order): Flow<Order> = callbackFlow {
        if (user != null) {
            val userId = user.uid
            val idOrder = order.id
            var photos = order.photos?.toMutableList()
            if (photos == null) {
                photos = mutableListOf(Photo(photoUri))
            } else {
                photos.add(Photo(photoUri))
            }
            val orderUpdate =
                Order(
                    id = idOrder,
                    status = order.status,
                    client = order.client,
                    dateAdd = order.dateAdd,
                    dateClose = 0,
                    description = order.description,
                    type = order.type,
                    model = order.model,
                    priceZip = order.priceZip,
                    priceWork = order.priceWork,
                    history = order.history,
                    photos = photos,
                    comment = order.comment,
                )

            database
                .child("users")
                .child(userId)
                .child("orders")
                .child(idOrder!!)
                .setValue(orderUpdate)

            val dbNewOrderUpdate = database
                .child("users")
                .child(userId)
                .child("orders")
                .child(idOrder)

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val updatedOrder = snapshot.getValue(Order::class.java)
                    if (updatedOrder != null) {
                        launch { send(updatedOrder) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            }
            dbNewOrderUpdate.addValueEventListener(valueEventListener)
            awaitClose { dbNewOrderUpdate.removeEventListener(valueEventListener) }
        }
    }

    fun addHistoryStep(order: Order, descStep: String): Flow<Order> = callbackFlow {
        if (user != null) {
            val userId = user.uid
            val idOrder = order.id
            val dateNewStep = DateUtil.dateOfUnit
            val history = order.history?.toMutableList()
            history?.let {
                val oldHistoryStep = history.last().apply {
                    type = 1
                }
                val odlIdHistoryStep = oldHistoryStep.id

                val newIdHistoryStep = odlIdHistoryStep.plus(1)
                val newHistoryStep = HistoryStep(
                    newIdHistoryStep,
                    dateNewStep,
                    2,
                    descStep
                )
                history.add(newHistoryStep)
                Log.d("TEST_history", history.toString())
            }

            val orderUpdate = Order(
                id = idOrder,
                status = order.status,
                client = order.client,
                dateAdd = order.dateAdd,
                dateClose = 0,
                description = order.description,
                type = order.type,
                model = order.model,
                priceZip = order.priceZip,
                priceWork = order.priceWork,
                history = history,
                photos = order.photos,
                comment = order.comment,
            )

            database
                .child("users")
                .child(userId)
                .child("orders")
                .child(idOrder!!)
                .setValue(orderUpdate)

            val dbNewOrderUpdate = database
                .child("users")
                .child(userId)
                .child("orders")
                .child(idOrder)

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val updatedOrder = snapshot.getValue(Order::class.java)
                    if (updatedOrder != null) {
                        launch { send(updatedOrder) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            }
            dbNewOrderUpdate.addValueEventListener(valueEventListener)
            awaitClose { dbNewOrderUpdate.removeEventListener(valueEventListener) }
        }
    }

    /* Clients */
    fun getAllClients(): Flow<List<Client>> = callbackFlow {
        //val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val db = database.child("users").child(userId).child("clients")
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clients = mutableListOf<Client>()
                    for (i in snapshot.children) {
                        val client = i.getValue(Client::class.java)
                        if (client != null) {
                            clients.add(client)
                        }
                    }
                    launch { send(clients) }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            }
            db.addValueEventListener(valueEventListener)
            awaitClose { db.removeEventListener(valueEventListener) }
        }
    }

    fun addClient(name: String, phone: List<String>): Flow<Client> = callbackFlow {
        if (user != null) {
            val userId = user.uid
            val idClient = database
                .child("users")
                .child(userId)
                .child("clients")
                .push().key.toString()
            val clientNew = Client(idClient, name, phone)
            database
                .child("users")
                .child(userId)
                .child("clients")
                .child(idClient)
                .setValue(clientNew)

            val dbNewClientUpdate = database
                .child("users")
                .child(userId)
                .child("clients")
                .child(idClient)

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val updatedClient = snapshot.getValue(Client::class.java)
                    if (updatedClient != null) {
                        launch { send(updatedClient) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            }
            dbNewClientUpdate.addValueEventListener(valueEventListener)
            awaitClose { dbNewClientUpdate.removeEventListener(valueEventListener) }


        }
    }

    fun editClient(id: String, name: String, phone: String): Flow<Client> = callbackFlow {
        if (user != null) {
            val userId = user.uid
            val clientUpdate = Client(
                id = id,
                name = name,
                phone = phone.split(", ")
            )
            database
                .child("users")
                .child(userId)
                .child("clients")
                .child(id)
                .setValue(clientUpdate)

            val dbNewClientUpdate = database
                .child("users")
                .child(userId)
                .child("clients")
                .child(id)

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val updatedClient = snapshot.getValue(Client::class.java)
                    if (updatedClient != null) {
                        launch { send(updatedClient) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            }
            dbNewClientUpdate.addValueEventListener(valueEventListener)
            awaitClose { dbNewClientUpdate.removeEventListener(valueEventListener) }
        }
    }

}