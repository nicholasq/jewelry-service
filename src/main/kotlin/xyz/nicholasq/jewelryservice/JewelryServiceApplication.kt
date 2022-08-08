package xyz.nicholasq.jewelryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JewelryServiceApplication

fun main(args: Array<String>) {
    runApplication<JewelryServiceApplication>(*args)
}
