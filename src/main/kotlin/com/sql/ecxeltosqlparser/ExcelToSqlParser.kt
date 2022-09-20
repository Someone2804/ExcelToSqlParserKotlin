package com.sql.ecxeltosqlparser

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.PrintWriter
import java.util.*

/**
 *   @project ecxel-to-sql-parser
 *   @author Sergey_Orudzhev on 20.09.2022
 */
class ExcelToSqlParser {

    fun parse(params: Array<String>) {
        val sheet: Sheet = WorkbookFactory.create(File(params[0])).getSheetAt(0)
        val fields = sheet.getRow(0).map { it.toString() }.toList()
        val file = File(params[1]).apply { createNewFile() }
        PrintWriter(file).use {
            sheet.drop(1).map { row ->
                val activeFields: MutableList<String> = mutableListOf()
                val values: MutableList<String> = mutableListOf()
                row.map {
                    activeFields.add(fields[it.columnIndex])
                    when (it.cellType) {
                        CellType.NUMERIC -> values.add(it.numericCellValue.toInt().toString())
                        else -> values.add(it.toString())
                    }
                }
                if (params[2].toBoolean()) {
                    activeFields.add("id")
                    values.add(UUID.randomUUID().toString())
                }
                it.println(createStatement(activeFields, values, sheet.sheetName))
            }
        }
    }

    private fun createStatement(fields: List<String>, values: List<String>, sheetName: String) =
        "INSERT INTO $sheetName ${fields.joinToString(", ", "(", ") ")} \n values ${
            values.joinToString(", ", "(", ")",
                transform = {
                    var value = it
                    if (value.contains("'")) {
                        value = value.replace("'", "''")
                    }
                    "'$value'"
                })
        };"
}