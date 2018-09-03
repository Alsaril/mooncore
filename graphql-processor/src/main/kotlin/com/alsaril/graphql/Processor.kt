package com.alsaril.graphql

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic


@AutoService(Processor::class)
class ListTypeAnnotationProcessor : AbstractProcessor() {
    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(S::class.java).forEach { ann ->

            val type = ann.asType()
            if (type.kind == TypeKind.DECLARED) {
                val args = (type as DeclaredType).typeArguments
                args.forEach { t -> processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, t.toString()) }
            }
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(S::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }
}