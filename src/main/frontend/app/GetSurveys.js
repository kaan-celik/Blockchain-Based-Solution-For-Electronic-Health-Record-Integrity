import React from "react";

import Grid from "@material-ui/core/Grid";
import SurveyItem from "./SurveyItem";



class GetSurveys extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      surveys: []
    }
  }

  componentDidMount() {
    this.fetchSurveys();
  }

  fetchSurveys() {
    fetch("/surveys").then(function (response) {
      if (response.ok) {
        response.json().then(function (data) {
          this.setState({
            surveys: data,
          });
        }.bind(this));
      } else {
        throw new Error(response.status);
      }
    }.bind(this));
  }


  render() {
    this.state.surveys.map(survey => console.log("surveys " + survey.appId));
    return (this.state.surveys.map(survey => (
        <Grid key={survey.appId} item xs={4}>
          <SurveyItem survey={survey}/>
        </Grid>
    ))
    );
  }
}

export default GetSurveys;

