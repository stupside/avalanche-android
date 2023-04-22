
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlanItem(
    name: String,
    description: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(name) },
        supportingContent = { Text(description) },
        trailingContent = {
            IconButton(onClick = {
                onClick()
            }) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = "Order $name")
            }
        }
    )
}