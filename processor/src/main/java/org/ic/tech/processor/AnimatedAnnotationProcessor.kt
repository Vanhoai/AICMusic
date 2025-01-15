package org.ic.tech.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class AnimatedAnnotationProcessor(
    environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    private val logger = environment.logger
    private val codeGenerator = environment.codeGenerator

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols = resolver.getSymbolsWithAnnotation(AnimatedAnnotation::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()


        symbols.forEach {
            val classname = it.simpleName.asString()
            val packageName = it.packageName.asString()
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = packageName,
                fileName = "${classname}_Generated"
            )

            file.bufferedWriter().use { writer ->
                writer.write("package $packageName \n \n")
                writer.write("class ${classname}Generated {\n")
                writer.write("    fun printName() = println(\"This is the generated code\") \n")
                writer.write("}\n")
            }

            logger.info("Generated file for $classname")
        }

        return emptyList()
    }
}

class AnimatedAnnotationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AnimatedAnnotationProcessor(environment)
    }
}