import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.databinding.RowSocialFollowingAllBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class SocialFollowingAllAdapter(): RecyclerView.Adapter<SocialFollowingAllAdapter.SocialFollowingAllViewHolder>() {
    private val items = mutableListOf<Member>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialFollowingAllViewHolder {
        val binding = RowSocialFollowingAllBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SocialFollowingAllViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialFollowingAllViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(socialFollowingAllRows: List<Member>) {
        items.clear()
        items.addAll(socialFollowingAllRows)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SocialFollowingAllViewHolder(private val binding: RowSocialFollowingAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Member) {
            binding.ivProfileImage.loadImage(member.memberProfileImage.toString())
            binding.tvProfileNickname.text = member.memberNickName
        }
    }
}