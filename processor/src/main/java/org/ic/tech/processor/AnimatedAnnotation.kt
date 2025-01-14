package org.ic.tech.processor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnimatedAnnotation(val value: String)