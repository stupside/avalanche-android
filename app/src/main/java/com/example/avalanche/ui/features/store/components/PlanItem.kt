import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avalanche.ui.components.AvalancheBadge
import java.util.concurrent.TimeUnit

@Composable
fun PlanItem(
    name: String,
    description: String,
    price: Int,
    free: Boolean,
    duration: Long,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable {
            onClick()
        },
        leadingContent = { Text(name) },
        headlineContent = { Text(description) },
        supportingContent = {

            val days = TimeUnit.SECONDS.toDays(duration)

            val text = if (days == 0L) {
                val minutes = TimeUnit.SECONDS.toMinutes(duration)

                "$minutes minutes"
            } else {
                "$days days"
            }

            Text(text)
        },
        trailingContent = {
            AvalancheBadge(isSuccess = free, successText = "Free", errorText = "${price / 100} $")
        }
    )
}