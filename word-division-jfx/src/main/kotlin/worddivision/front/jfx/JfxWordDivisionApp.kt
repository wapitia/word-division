package com.wapitia.games.worddivision.front.jfx

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import com.wapitia.games.worddivision.model.Tableau

class JfxWordDivisionApp : Application() {

     var model : Tableau? = null

    /**
     * A Single-character entry component for Tableau user interaction UI
     */
    class TableauTextCharWidget : TextField() {

        val colSpacing = 32.0
        val rowSpacing = 34.0

        init {
            this.insets
            minWidth = colSpacing - 2
            prefWidth = colSpacing - 2
            minHeight = rowSpacing - 2
            prefHeight = rowSpacing - 2
        }
    }

    data class WordDivisionScene(
        val title: String = "Word Division | Wapitia"
    ) {
        fun stage(primaryStage: Stage) {
            primaryStage.title = title
            val btn = Button()
            btn.text = "Say 'Hello World'"
            btn.onAction = EventHandler<ActionEvent> { println("Hello World!") }

            val root = Pane()

            val colSpacing = 32.0
            val rowSpacing = 34.0

            for (row in 0 .. 9) {
                for (col in 0 .. 9 ) {
                    val charField = TableauTextCharWidget()
                    charField.relocate(col * colSpacing, row * rowSpacing)
                    root.children.add(charField)
                }

            }

            root.children.add(btn)
            primaryStage.scene = Scene(root, 400.toDouble(), 290.toDouble())
        }
    }

    val defaultStaging = WordDivisionScene()

    val title = "Word Division | Wapitia"

    override fun start(primaryStage: Stage) {
        defaultStaging.stage(primaryStage)
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(JfxWordDivisionApp::class.java, *args)
        }
    }
}