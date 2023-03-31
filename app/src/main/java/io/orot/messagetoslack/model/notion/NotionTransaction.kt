package io.orot.messagetoslack.model.notion

data class NotionTransaction(
    val parent: NotionTransactionParent,
    val properties: NotionTransactionProperties,
)

data class NotionTransactionParent(
    val database_id: String = "f1f10203-7d27-481a-abd0-9703a2e8b29f"
)

data class NotionTransactionProperties(
    val 날짜: NotionTransactionPropertiesDate,
    val 금액: NotionTransactionPropertiesPrice,
    val 내역: NotionTransactionPropertiesContent,
    val 결제방법: NotionTransactionPropertiesPayment
)

data class NotionTransactionPropertiesDate(
    val date: NotionTransactionPropertiesDateDetail
)

data class NotionTransactionPropertiesDateDetail(
    val start: String = ""
)

data class NotionTransactionPropertiesPrice(
    val number: Int
)

data class NotionTransactionPropertiesContent(
    val title: List<NotionTransactionPropertiesContentInfo>
)

data class NotionTransactionPropertiesContentInfo(
    val type: String,
    val text: NotionTransactionPropertiesContentInfoText,
)
data class NotionTransactionPropertiesContentInfoText(
    val content: String,
)

data class NotionTransactionPropertiesPayment(
    val select: NotionTransactionPropertiesPaymentInfo
)
data class NotionTransactionPropertiesPaymentInfo(
    val name: String
)

