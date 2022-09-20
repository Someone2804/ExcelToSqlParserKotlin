package com.sql.ecxeltosqlparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExcelToSqlParserApplication

fun main(args: Array<String>) {
    runApplication<ExcelToSqlParserApplication>(*args)
    ExcelToSqlParser().parse(args)
}
