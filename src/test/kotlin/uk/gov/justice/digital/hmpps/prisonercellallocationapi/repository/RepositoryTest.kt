package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import jakarta.transaction.Transactional
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.SqlMergeMode

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
abstract class RepositoryTest
