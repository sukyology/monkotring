package com.example.monkotring

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class MonkotringApplication {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<MonkotringApplication>(*args)
        }
    }
}

@Document
data class HomenetSetting(
    val vendor: Vendor
) {
    @MongoId
    lateinit var id: String

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.DEDUCTION,
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = Vendor.HYUNDAI_HT::class, name = "HyundaiHt"),
        JsonSubTypes.Type(value = Vendor.WHAT::class, name = "What"),
    )
    sealed class Vendor {
        class HYUNDAI_HT(
            val protocol: Protocol
        ) : Vendor() {
            enum class Protocol {
                OTP, MASTER_USER
            }
        }

        class WHAT(
            val name: String
        ) : Vendor()
    }
}

interface HomenetSettingRepository : MongoRepository<HomenetSetting, String>

@RestController
@RequestMapping
class Controller(
    private val homenetSettingRepository: HomenetSettingRepository
) {

    @RequestMapping
    fun get() = homenetSettingRepository.findAll()

    @PostMapping
    fun save(@RequestBody homenetSetting: HomenetSetting) = homenetSettingRepository.save(homenetSetting)

}