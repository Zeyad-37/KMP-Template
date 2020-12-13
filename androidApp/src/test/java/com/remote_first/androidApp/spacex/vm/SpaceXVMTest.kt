package com.remote_first.androidApp.spacex.vm

import com.badoo.reaktive.scheduler.trampolineScheduler
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.remote_first.androidApp.emitsEffects
import com.remote_first.androidApp.emitsNoEffects
import com.remote_first.androidApp.emitsNoErrors
import com.remote_first.androidApp.emitsStates
import com.remote_first.androidApp.initWith
import com.remote_first.androidApp.space_x.FullState
import com.remote_first.androidApp.space_x.InitialState
import com.remote_first.androidApp.space_x.SpaceEffect
import com.remote_first.androidApp.space_x.vm.SpaceXInputHandler2
import com.remote_first.androidApp.space_x.vm.SpaceXReducer
import com.remote_first.androidApp.space_x.vm.SpaceXVM
import com.remote_first.androidApp.space_x.vm.mapToPO
import com.remote_first.androidApp.verifyThat
import com.remote_first.shared.flow_redux.EmptyOutcome
import com.remote_first.shared.space_x.EmptyUseCase
import com.remote_first.shared.space_x.GetLaunchesUseCase
import com.remote_first.shared.space_x.SpaceEffectUseCase
import com.remote_first.shared.space_x.domain_objects.Links
import com.remote_first.shared.space_x.domain_objects.Rocket
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import com.remote_first.shared.space_x.domain_objects.SpaceEffectK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@FlowPreview
@ExperimentalCoroutinesApi
internal class SpaceXVMTest {

    private val getLaunchesUseCase: GetLaunchesUseCase = mock()
    private val spaceEffectUseCase: SpaceEffectUseCase = mock()
    private val emptyUseCase: EmptyUseCase = mock()
    private val spaceXInputHandler2 = SpaceXInputHandler2(getLaunchesUseCase, spaceEffectUseCase, emptyUseCase)
    private val spaceXReducer = SpaceXReducer()
    private lateinit var spaceXVM: SpaceXVM

    @BeforeEach
    fun setUp() {
        reset(getLaunchesUseCase, spaceEffectUseCase, emptyUseCase)
        spaceXVM = SpaceXVM(spaceXInputHandler2, spaceXReducer, null, trampolineScheduler, trampolineScheduler)
    }

    @Test
    fun `on processGetLaunches then return FullState`() {
        val listOfLaunches = listOf(RocketLaunch(flightNumber = 137,
                missionName = "name",
                launchYear = 2020,
                launchDateUTC = "utc",
                rocket = Rocket(id = "1", name = "rocket", type = "type"),
                details = "details",
                launchSuccess = true,
                links = Links(missionPatchUrl = "url", articleUrl = null)))
        val listOfLaunchesPO = mapToPO(listOfLaunches)
        whenever(getLaunchesUseCase.execute()).thenReturn(flow { emit(listOfLaunches) })

        spaceXVM.initWith(InitialState)
                .verifyThat { processGetLaunches() }
                .emitsStates(InitialState, FullState(listOfLaunchesPO))
                .emitsNoEffects()
                .emitsNoErrors()

        verify(getLaunchesUseCase).execute()
        verify(spaceEffectUseCase, never()).execute()
        verify(emptyUseCase, never()).execute()
    }

    @Test
    fun `on processShowEffect then return SpaceEffect`() {
        whenever(spaceEffectUseCase.execute()).thenReturn(SpaceEffectK)

        spaceXVM.initWith(InitialState)
                .verifyThat { processShowEffect() }
                .emitsStates(InitialState)
                .emitsEffects(SpaceEffect)
                .emitsNoErrors()

        verify(spaceEffectUseCase).execute()
        verify(getLaunchesUseCase, never()).execute()
        verify(emptyUseCase, never()).execute()
    }

    @Test
    fun `on processDoNothing then DoNothing`() {
        whenever(emptyUseCase.execute()).thenReturn(EmptyOutcome)

        spaceXVM.initWith(InitialState)
                .verifyThat { processDoNothing() }
                .emitsStates(InitialState)
                .emitsNoEffects()
                .emitsNoErrors()

        verify(emptyUseCase).execute()
        verify(getLaunchesUseCase, never()).execute()
        verify(spaceEffectUseCase, never()).execute()
    }
}