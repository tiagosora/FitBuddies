import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Participant(
    val id: String,
    val name: String,
    val avatarUrl: String
)

data class ChallengeDetails(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val type: String,
    val duration: String,
    val goal: Int,
    val goalCompletion : Int,
    val participants: List<Participant>
)

class ChallengeDetailsViewModel : ViewModel() {
    private val _challengeDetails = MutableStateFlow<ChallengeDetails?>(null)
    val challengeDetails: StateFlow<ChallengeDetails?> = _challengeDetails.asStateFlow()

    fun loadChallengeDetails(challengeId: String) {
        viewModelScope.launch {
            // In a real app, you would fetch this data from a repository or API
            _challengeDetails.value = ChallengeDetails(
                id = challengeId,
                name = "30-Day Running Challenge",
                imageUrl = "https://example.com/running-challenge.jpg",
                description = "Run every day for 30 days to improve your endurance and overall fitness. You can do this bestie, I believe in you you know that hahhahahah slay queen aaaaa",
                duration = "30 days",
                type = "Running",
                goal = 30,
                goalCompletion = 20,
                participants = listOf(
                    Participant("1", "John Doe", "https://example.com/john.jpg"),
                    Participant("2", "Jane Smith", "https://example.com/jane.jpg"),
                    Participant("3", "Mike Johnson", "https://example.com/mike.jpg")
                )
            )
        }
    }

    fun onQRCodeClick() {
        // TODO: Implement logic to open QR code in a modal view
    }
}





