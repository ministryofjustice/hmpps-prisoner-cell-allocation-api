package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "cell_movement")
data class CellMovement(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
  val agency: String,
  val cellId: Long,
  val cellDescription: String,
  val prisonerId: String,
  val prisonerName: String,
  val userId: String,
  val dateTime: LocalDateTime,
  val reason: String,
  @Enumerated(EnumType.STRING)
  val moveType: MoveType,
)

enum class MoveType {
  IN, OUT
}
