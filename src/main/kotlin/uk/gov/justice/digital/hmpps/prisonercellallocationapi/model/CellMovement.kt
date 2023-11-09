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
  val agency: String? = null,
  val cellId: Long? = null,
  val cellDescription: String? = null,
  val prisonerId: String? = null,
  val prisonerName: String? = null,
  val userId: String? = null,
  val dateTime: LocalDateTime? = null,
  val reason: String? = null,
  @Enumerated(EnumType.STRING)
  val direction: Direction? = null,
)

enum class Direction {
  IN, OUT
}
