package org.hinsun.processors

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnimatedAnnotation(val value: String)