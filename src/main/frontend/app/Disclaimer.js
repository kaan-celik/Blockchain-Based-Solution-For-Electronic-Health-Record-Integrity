import Typography from "@material-ui/core/Typography";
import * as React from "react";

class Disclaimer extends React.Component{

  render() {
    return (
        <Typography align={'left'}
                    style={{
                      fontSize: '12px',
                      marginTop: '-14px',
                      marginLeft: '15px'
                    }}
                    color="textSecondary">
          This application never keep your keys for any reason. The protection of your accounts, information,
          passwords, keys are under your responsibilities. </Typography>
    )
  }
}

export default Disclaimer;