package ro.asis.guest.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication(
    scanBasePackages = [
        "ro.asis.commons",
        "ro.asis.guest.client",
        "ro.asis.guest"
    ]
)
class GuestServiceApplication

fun main(args: Array<String>) {
    runApplication<GuestServiceApplication>(*args)
}
