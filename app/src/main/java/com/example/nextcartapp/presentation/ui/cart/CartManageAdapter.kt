import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.R
import com.example.nextcartapp.domain.model.Cart

class CartManageAdapter(
    private val onCartClick: (Cart) -> Unit,
    private val onDeleteClick: (Cart) -> Unit
) : ListAdapter<Cart, CartManageAdapter.ViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_manage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = getItem(position)
        holder.bind(cart, onCartClick, onDeleteClick)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tvCartName)
        private val card: View = view.findViewById(R.id.cardCart)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(cart: Cart, onCartClick: (Cart) -> Unit, onDeleteClick: (Cart) -> Unit) {
            tvName.text = cart.name
            card.setOnClickListener { onCartClick(cart) }
            btnDelete.setOnClickListener { onDeleteClick(cart) }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart) = oldItem.cartId == newItem.cartId
        override fun areContentsTheSame(oldItem: Cart, newItem: Cart) = oldItem == newItem
    }
}