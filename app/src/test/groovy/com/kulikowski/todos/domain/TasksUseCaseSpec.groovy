package com.kulikowski.todos.domain

import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.network.ToDoApiService
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Response
import spock.lang.Specification

class TasksUseCaseSpec extends Specification {

    def "should store proper lastEnd position after getting success response"() {
        given:
            def toDosRepository = GroovyMock(ToDosRepository)
            def toDosApiService = GroovyMock(ToDoApiService)
            def headers = new Headers.Builder().add("X-Total-Count", "200").build()
            def successResponse = Response.success(new ArrayList(), headers)
            def objectUnderTest = new TasksUseCase(toDosApiService, toDosRepository)
        when:
            objectUnderTest.handleResponse(successResponse, 0, 20)
        then:
            objectUnderTest.lastEnd == 20
    }

    def "should keep initial end position value after getting failure response"() {
        given:
            def toDosRepository = GroovyMock(ToDosRepository)
            def toDosApiService = GroovyMock(ToDoApiService)
            def responseBody = Mock(ResponseBody)
            def successResponse = Response.error(400, responseBody)
            def objectUnderTest = new TasksUseCase(toDosApiService, toDosRepository)
        when:
            objectUnderTest.handleResponse(successResponse, 0, 20)
        then:
            objectUnderTest.lastEnd == 0
    }

    def "should indicate that there are still next tasks for fetch "() {
        given:
            def toDosRepository = GroovyMock(ToDosRepository)
            def toDosApiService = GroovyMock(ToDoApiService)
            def headers = new Headers.Builder().add("X-Total-Count", "200").build()
            def successResponse = Response.success(new ArrayList(), headers)
            def objectUnderTest = new TasksUseCase(toDosApiService, toDosRepository)
        when:
            objectUnderTest.handleResponse(successResponse, 0, 199)
        then:
            objectUnderTest.hasNext()
    }

    def "should indicate that there are not next tasks for fetch "() {
        given:
            def toDosRepository = GroovyMock(ToDosRepository)
            def toDosApiService = GroovyMock(ToDoApiService)
            def headers = new Headers.Builder().add("X-Total-Count", "200").build()
            def successResponse = Response.success(new ArrayList(), headers)
            def objectUnderTest = new TasksUseCase(toDosApiService, toDosRepository)
        when:
            objectUnderTest.handleResponse(successResponse, 0, 200)
        then:
            !objectUnderTest.hasNext()
    }

}
