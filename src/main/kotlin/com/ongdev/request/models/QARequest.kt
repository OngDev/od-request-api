package com.ongdev.request.models

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "qna")
class QARequest : BaseRequest()