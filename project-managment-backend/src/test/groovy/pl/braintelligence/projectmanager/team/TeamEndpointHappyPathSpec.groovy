package pl.braintelligence.projectmanager.team

import pl.braintelligence.projectmanager.base.BaseIntegrationSpec
import pl.braintelligence.projectmanager.team.base.OperatingOnTeamEndpoint
import pl.braintelligence.projectmanager.team.base.SampleTeamMemberDto
import spock.lang.Unroll

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import static pl.braintelligence.projectmanager.team.base.SampleNewTeamDto.sampleNewTeamDto
import static pl.braintelligence.projectmanager.team.base.SampleTeamMemberDto.sampleTeamMemberDto

class TeamEndpointHappyPathSpec extends BaseIntegrationSpec implements OperatingOnTeamEndpoint {

    def "Should not create a team that already exists"() {
        when:
        def response = createNewTeam(sampleNewTeamDto())

        then: "system response - team is created"
        response.statusCode == CREATED

        when: "user asks for all created teams"
        response = getExistingTeams()

        then: "checks that one team was created and has default settings"
        response.statusCode == OK
        response.body.size() == 1
        response.body[0].name == sampleNewTeamDto().name
        response.body[0].currentlyImplementedProjects == 0
        response.body[0].busy == false
        response.body[0].members == []

        when: "member is added to a team"
        response = addMemberToTeam(sampleTeamMemberDto(), sampleNewTeamDto())

        then: "system response - member is added"
        response.statusCode == CREATED

        when: "user asks again for all created teams"
        response = getExistingTeams()

        then: "checks that member was added"
        response.body[0].members[0] == SampleTeamMemberDto.TEAM_MEMBER_DTO
    }
}
