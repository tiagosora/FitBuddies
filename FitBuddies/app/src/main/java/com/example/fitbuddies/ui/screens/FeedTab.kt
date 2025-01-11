package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitbuddies.R

@Composable
fun FeedTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabeçalho simples
        item {
            Text(
                text = "Recent Posts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 1º post (sem imagem)
        item {
            PostCard(
                userName = "John Doe",
                postTime = "2h ago",
                content = "Fiz um treino de corrida de 10 km hoje de manhã!",
                likeCount = 12,
                commentCount = 4,
                shareCount = 2
            )
        }
        // 2º post (com imagem)
        item {
            PostCard(
                userName = "Jane Smith",
                postTime = "5h ago",
                content = "Testei um treino HIIT de 20 minutos. Alguém tem dicas?",
                likeCount = 8,
                commentCount = 1,
                shareCount = 0,
                imageResId = R.drawable.pic_for_feed_background
            )
        }
        // 3º post (sem imagem)
        item {
            PostCard(
                userName = "Carlos F.",
                postTime = "1d ago",
                content = "Descobri um novo parque para correr perto de casa. Recomendo!",
                likeCount = 20,
                commentCount = 5,
                shareCount = 3
            )
        }
    }
}

/**
 * Card estático que mostra um post fictício no feed,
 * incluindo ícone de usuário, texto e botões de interação (like/comment/share).
 * Agora com a opção de exibir uma imagem (imageResId) no conteúdo.
 */
@Composable
fun PostCard(
    userName: String,
    postTime: String,
    content: String,
    likeCount: Int,
    commentCount: Int,
    shareCount: Int,
    imageResId: Int? = null  // Parâmetro opcional para exibir imagem
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Linha superior: avatar + nome + tempo
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar (ícone do usuário)
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                // Nome e tempo
                Column {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = postTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Conteúdo do post
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )

            // Se houver uma imagem, exibe abaixo do texto
            if (imageResId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(imageResId),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)  // altura mínima para a imagem
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ícones alinhados à direita e menores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                InteractionIcon(
                    icon = Icons.Default.FavoriteBorder,
                    label = likeCount.toString()
                )
                Spacer(modifier = Modifier.width(12.dp))
                InteractionIcon(
                    icon = Icons.Default.Comment,
                    label = commentCount.toString()
                )
                Spacer(modifier = Modifier.width(12.dp))
                InteractionIcon(
                    icon = Icons.Default.Share,
                    label = shareCount.toString()
                )
            }
        }
    }
}

/**
 * Botão de interação (like, comment, share) com ícone e texto menores,
 * alinhados próximos.
 */
@Composable
fun InteractionIcon(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { /* TODO: Ação de clique */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)  // Ícone menor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall, // tipografia menor
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
