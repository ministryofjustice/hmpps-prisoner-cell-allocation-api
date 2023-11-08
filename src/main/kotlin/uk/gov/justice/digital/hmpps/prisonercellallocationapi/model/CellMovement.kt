package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model

import jakarta.persistence.Entity
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
  val cellId: Long? = null,
  val cellDescription: String? = null,
  val prisonerId: String,
  val prisonerName: String,
  val userId: String,
  val dateTime: LocalDateTime,
  val reason: String,
)
