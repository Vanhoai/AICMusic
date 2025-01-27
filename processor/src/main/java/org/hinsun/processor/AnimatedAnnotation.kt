package org.hinsun.processor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnimatedAnnotation(val value: String)