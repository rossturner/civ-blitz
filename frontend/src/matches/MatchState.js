
const matchStates = {
    SIGNUPS: 'Signups',
    DRAFT: 'Draft',
    IN_PROGRESS: 'In progress',
    POST_MATCH: 'Post-match',
    COMPLETED: 'Completed'
}

const matchStateToString = (enumValue) => {
    return matchStates[enumValue];
}

export  { matchStateToString };